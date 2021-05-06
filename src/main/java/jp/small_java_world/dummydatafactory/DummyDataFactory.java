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
		// targetClassとその親のフィールドを取得
		Field[] ownFields = targetClass.getDeclaredFields();
		Field[] superFields = targetClass.getSuperclass().getDeclaredFields();

		// targetClassとその親のフィールドをfieldsにセット
		Field[] fields = new Field[ownFields.length + (superFields != null ? superFields.length : 0)];
		System.arraycopy(ownFields, 0, fields, 0, ownFields.length);
		if (superFields != null) {
			System.arraycopy(superFields, 0, fields, ownFields.length, superFields.length);//
		}

		// キー：Field#getName()、値:SqlColumnData
		// 厳密にはキーはdbのカラム名をキャメルケースに変換した値
		Map<String, SqlColumnData> sqlColumnDataMap = new HashMap<>();

		// isEntity=trueの場合は対応するcreate sqlの中身を読み込んでMap<String, SqlColumnData>を生成
		if (isEntity) {
			String sqlContent = SqlFileUtil.getSqlContent(targetClass.getSimpleName());
			if (sqlContent != null) {
				sqlColumnDataMap = SqlAnalyzer.getSqlColumnDataMap(sqlContent);
			}
		}

		// 生成対象のクラスのインスタンスを生成
		Constructor<T> constructor = targetClass.getDeclaredConstructor();
		constructor.setAccessible(true);
		T entity = constructor.newInstance();

		// 対象フィールドをループし、フィールドに対応するダミーデータの生成とentityへのセットを行う。
		for (Field field : fields) {
			Class<?> type = field.getType();
			String fieldName = field.getName();
			int modifiers = field.getModifiers();
			if (Modifier.isFinal(modifiers)) {
				continue;
			}

			// 実際のダミーデータの生成
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

		// targetClassListに対応するインスタンスを生成し、各targetClass.getSimpleName()をキーとして、resultMapにインスタンスを値として格納
		for (var targetClass : targetClassList) {
			resultMap.put(targetClass.getSimpleName(), generateDummyInstance(targetClass, true));
		}

		// relationMapに設定されている情報を利用して値をコピー
		// relationMap = Map.of("FugaEntity.fugaId",
		// "HogeEntity.id"))の場合、FugaEntity.fugaIdにHogeEntity.idをコピー
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