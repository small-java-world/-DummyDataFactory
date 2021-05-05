package jp.small_java_world.dummydatafactory.util;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import jp.small_java_world.dummydatafactory.entity.DummyEntity;
import jp.small_java_world.dummydatafactory.entity.DummyStaticEntity;

class ReflectUtilTest {
	@Test
	void testSetStaticFieldValue() throws Exception {
		ReflectUtil.setStaticFieldValue(DummyStaticEntity.class, "staticIntegerMember", 10);
		assertEquals(10, DummyStaticEntity.getStaticIntegerMember());
	}

	@Test
	void testSetFieldValue() throws Exception {
		DummyEntity dummyEntity = new DummyEntity();
		ReflectUtil.setFieldValue(dummyEntity, "integerMember", 20);
		assertEquals(20, dummyEntity.getIntegerMember());
	}

	@Test
	void testGetFieldValue() throws Exception {
		DummyEntity dummyEntity = new DummyEntity();
		dummyEntity.setShortMember((short) 30);
		ReflectUtil.getFieldValue(dummyEntity, "shortMember");
		assertEquals((short) 30, dummyEntity.getShortMember());
	}
	
	@Test
	void testGetDeclaredField() throws Exception {
		DummyEntity dummyEntity = new DummyEntity();
		dummyEntity.setShortMember((short) 30);
		Field field = ReflectUtil.getDeclaredField(DummyEntity.class, "shortMember");
		assertEquals("shortMember", field.getName());
	}
}
