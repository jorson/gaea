package gaea.platform.security;


import gaea.platform.security.support.ResourceMatchType;

public interface ResourceDefinitionResolver {

	/**
	 * 使用默认方式预处理URL字符串
	 * 
	 * @param url
	 * @return
	 */
	public String preProcessUrl(String url);

	/**
	 * 根据是否使用UseAntPath和是否字符小写化来预先格式化url
	 * 
	 * @param url
	 *            URL字符串
	 * @param isUseAntPath
	 *            是否使用Ant Path
	 * @param ignoreCase
	 *            是否要忽略大小写
	 * @return
	 */
	public String preProcessUrl(String url, boolean isUseAntPath, boolean ignoreCase);

	/**
	 * 请求的URL是否与资源URL匹配
	 * <p/>
	 * 默认的方式匹配。
	 * 
	 * @param requestUrl
	 *            请求的URL
	 * @param resourceUrl
	 *            资源的URL
	 * @return
	 */
	public boolean isResourceMatch(String requestUrl, String resourceUrl);

	/**
	 * 请求的URL是否与资源URL匹配
	 * 
	 * @param requestUrl
	 *            请求的URL
	 * @param resourceUrl
	 *            资源的URL
	 * @param type
	 *            类型
	 * @return
	 */
	public boolean isResourceMatch(String requestUrl, String resourceUrl, ResourceMatchType type);

}
