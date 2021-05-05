package jp.small_java_world.dummydatafactory.entity;

public class DummyStaticEntity {
	static int staticIntegerMember;
	
	public static int getStaticIntegerMember() {
		return staticIntegerMember;
	}
	public static void setStaticIntegerMember(int staticIntegerMember) {
		DummyStaticEntity.staticIntegerMember = staticIntegerMember;
	}
}
