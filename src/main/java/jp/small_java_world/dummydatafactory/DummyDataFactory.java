package jp.small_java_world.dummydatafactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.small_java_world.dummydatafactory.data.SqlColumnData;
import jp.small_java_world.dummydatafactory.util.ReflectUtil;
import jp.small_java_world.dummydatafactory.util.SqlAnalyzer;
import jp.small_java_world.dummydatafactory.util.SqlFileUtil;

public class DummyDataFactory {
	private static final Logger logger = LoggerFactory.getLogger(DummyDataFactory.class);

	public static <T> T generateDummyInstance(Class<T> targetClass, boolean isEntity) throws Exception {
		// targetClass�Ƃ��̐e�̃t�B�[���h���擾
		Field[] ownFields = targetClass.getDeclaredFields();
		Field[] superFields = targetClass.getSuperclass().getDeclaredFields();

		// targetClass�Ƃ��̐e�̃t�B�[���h��fields�ɃZ�b�g
		Field[] fields = new Field[ownFields.length + (superFields != null ? superFields.length : 0)];
		System.arraycopy(ownFields, 0, fields, 0, ownFields.length);
		if (superFields != null) {
			System.arraycopy(superFields, 0, fields, ownFields.length, superFields.length);//
		}

		// �L�[�FField#getName()�A�l:SqlColumnData
		// �����ɂ̓L�[��db�̃J���������L�������P�[�X�ɕϊ������l
		Map<String, SqlColumnData> sqlColumnDataMap = new HashMap<>();

		// isEntity=true�̏ꍇ�͑Ή�����create sql�̒��g��ǂݍ����Map<String, SqlColumnData>�𐶐�
		if (isEntity) {
			String sqlContent = SqlFileUtil.getSqlContent(targetClass.getSimpleName());
			if (sqlContent != null) {
				sqlColumnDataMap = SqlAnalyzer.getSqlColumnDataMap(sqlContent);
			}
		}

		// �����Ώۂ̃N���X�̃C���X�^���X�𐶐�
		Constructor<T> constructor = targetClass.getDeclaredConstructor();
		constructor.setAccessible(true);
		T entity = constructor.newInstance();

		// �Ώۃt�B�[���h�����[�v���A�t�B�[���h�ɑΉ�����_�~�[�f�[�^�̐�����entity�ւ̃Z�b�g���s���B
		for (Field field : fields) {
			Class<?> type = field.getType();
			String fieldName = field.getName();
			int modifiers = field.getModifiers();
			if (Modifier.isFinal(modifiers)) {
				continue;
			}

			// ���ۂ̃_�~�[�f�[�^�̐���
			Object fieldValue = RandomValueGenerator.generateRandomValue(type, sqlColumnDataMap.get(fieldName));

			try {
				field.setAccessible(true);
				field.set(entity, fieldValue);
			} catch (Exception e) {
				logger.info("Exception occurred in generateTestEntity ", e);
				logger.error("set value fail entityClass={} fieldName={} fieldValue={}", targetClass.getPackageName(),
						fieldName, fieldValue);
			}
		}

		return entity;
	}

	public static Map<String, Object> generateDummyEntities(List<Class<?>> targetClassList) throws Exception {
		return generateDummyEntities(targetClassList, new HashMap<String, String>());
	}

	public static Map<String, Object> generateDummyEntities(List<Class<?>> targetClassList,
			Map<String, String> relationMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// targetClassList�ɑΉ�����C���X�^���X�𐶐����A�etargetClass.getSimpleName()���L�[�Ƃ��āAresultMap�ɃC���X�^���X��l�Ƃ��Ċi�[
		for (var targetClass : targetClassList) {
			resultMap.put(targetClass.getSimpleName(), generateDummyInstance(targetClass, true));
		}

		// relationMap�ɐݒ肳��Ă�����𗘗p���Ēl���R�s�[
		// relationMap = Map.of("FugaEntity.fugaId",
		// "HogeEntity.id"))�̏ꍇ�AFugaEntity.fugaId��HogeEntity.id���R�s�[
		for (var entry : relationMap.entrySet()) {
			for (var toTargetClass : targetClassList) {
				if (entry.getKey().startsWith(toTargetClass.getSimpleName())) {
					String toMemberName = entry.getKey().replace(toTargetClass.getSimpleName() + ".", "");
					for (var fromTargetClass : targetClassList) {
						if (entry.getValue().startsWith(fromTargetClass.getSimpleName())) {
							String fromMemberName = entry.getValue().replace(fromTargetClass.getSimpleName() + ".", "");
							Object fromObject = resultMap.get(fromTargetClass.getSimpleName());
							Object toObject = resultMap.get(toTargetClass.getSimpleName());
							Object fromValue = ReflectUtil.getFieldValue(fromObject, fromMemberName);

							ReflectUtil.setFieldValue(toObject, toMemberName, fromValue);
							break;
						}
					}
				}
			}
		}

		return resultMap;
	}
}