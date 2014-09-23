package gaea.platform.security.support;

/**
 * 资源路径匹配类型的枚举值，不区分大小写
 * 
 * @author wuhy
 */
public enum ResourceMatchType {
	/**
	 * ANT路径方式匹配
	 */
	ANT("ant"),
	/**
	 * URL方式匹配，支持参数
	 */
	URL("url"),
	/**
	 * 正则表达式匹配
	 */
	REGULAR("regular");

	private String value;

	ResourceMatchType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static ResourceMatchType getEnum(String type) {
		for (ResourceMatchType t : ResourceMatchType.values()) {
			if (type.equalsIgnoreCase(t.value())) {
				return t;
			}
		}
		return null;
	}
}
