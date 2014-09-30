package gaea.platform.web.controller;

import gaea.foundation.core.utils.ObjectUtils;
import gaea.platform.web.controller.support.ControllerUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * 控制器的页面转向类
 *
 * @author bifeng.liu
 */
public abstract class ActionRedirector {

    /**
     * Controller页面的默认前缀
     */
    public static final String CONTROLLER_COMMON_PAGE_PREFIX = "/pages/";
    /**
     * 列表返回给Action的KEY
     */
    public static final String SUCCESS = "success";

    public ActionRedirector() {

    }

    /**
     * 跳转处理
     *
     * @param request
     * @param response
     * @param modelMap
     * @param isJson
     * @return
     */
    public Object redirect(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, boolean isJson) {
        if (!isJson) {
            return redirect(request, response, modelMap);
        } else {
            Throwable ex = null;
            try {
                Object obj = doAction(request, response, modelMap);
                return ObjectUtils.toJson(obj);
            } catch (Throwable e) {
                ex = e;
                if (e instanceof InvocationTargetException) {
                    InvocationTargetException targetException = (InvocationTargetException) e;
                    ex = targetException.getTargetException() != null ? targetException.getTargetException() : targetException;
                }
            }
            ErrorControllerResult actionResult = new ErrorControllerResult();
            actionResult.setMessage(ex.getMessage());
            return ObjectUtils.toJson(actionResult);
        }
    }

    /**
     * 跳转处理
     *
     * @param request
     * @param response
     * @param modelMap
     * @return
     */
    public ModelAndView redirect(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        ModelAndView result = null;
        try {
            Object obj = doAction(request, response, modelMap);
            if (obj instanceof String) {
                result = new ModelAndView((String) obj, modelMap);
            } else if (obj instanceof ModelAndView) {
                result = (ModelAndView) obj;
            } else {
                //modelMap.put(obj.getClass().getSimpleName(), obj);
                result = new ModelAndView(ControllerUtils.concatViewPagePath(CONTROLLER_COMMON_PAGE_PREFIX, SUCCESS), modelMap);
                result.addObject(obj.getClass().getSimpleName(), obj);
            }
        } catch (Throwable e) {
            ErrorControllerResult actionResult = new ErrorControllerResult();
            actionResult.setMessage("");
            actionResult.setException(e);
            result = new ModelAndView("/errors/error", modelMap);
            result.addObject("actionResult", actionResult);
        }
        return result;
    }

    protected abstract Object doAction(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Throwable;

}
