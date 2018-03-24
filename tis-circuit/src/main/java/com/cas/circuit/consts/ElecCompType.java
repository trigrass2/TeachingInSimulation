package com.cas.circuit.consts;

public enum ElecCompType {
	Relay(1, "继电器"), Accontactor(2, "交流接触器"), Switch(3, "开关"), Base(4, "底座"), Breaker(5, "断路器");

	private int type;
	private String name;

	ElecCompType(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public static ElecCompType getElecCompType(int type) {
		for (ElecCompType elecCompType : values()) {
			if (elecCompType.type == type) {
				return elecCompType;
			}
		}
		throw new RuntimeException("不支持的元器件类型" + type);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
