package yibao.yiwei.controller.system;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import yibao.yiwei.entity.system.Manager;

/**
 * 拦截器
 * 
 * @author Administrator
 * 
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String webApp = request.getContextPath();// 根目录
		Manager manager = (Manager)request.getSession().getAttribute("manager");
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			AuthPassport authPassport = ((HandlerMethod) handler).getMethodAnnotation(AuthPassport.class);
			// 没有声明需要权限,或者声明不验证权限
			if (authPassport == null || authPassport.validate() == false) {
				if (manager != null) {// 如果验证成功
					return true;
				} else {
					PrintWriter pw = response.getWriter();
					pw.print("<script type='text/JavaScript'> parent.parent.window.location.href='"+ webApp + "/toLogin' </script>");
					pw.flush();
					pw.close();
					return false;
				}
			} else {
				if (manager != null) {// 如果验证成功
					return true;
				} else {
					JOptionPane.showMessageDialog(null, "请先登录!", "系统提示", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		} else
			return true;
	}
}