import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet Filter implementation class LoginFilter
 */
/*@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        System.out.println("LoginFilter: " + httpRequest.getRequestURI());
        
        // Check if this URL is allowed to access without logging in
        if (this.isUrlAllowedWithoutLogin(httpRequest.getRequestURI())) {
        	System.out.println("This url is allowed");
            // Keep default action: pass along the filter chain
            chain.doFilter(request, response);
            return;
        }
        else {
        	System.out.println(httpRequest.getRequestURI() + "is not allowed to access without logging in.");
        }
        
        // Redirect to login page if the "user" attribute doesn't exist in session
        if (httpRequest.getSession().getAttribute("user") == null) {
        	System.out.println("user attribute doesnt exist in session");
            //httpResponse.sendRedirect("login.html");
        } else {
            chain.doFilter(request, response);
        }
    }

    // Setup your own rules here to allow accessing some resources without logging in
    // Always allow your own login related requests(html, js, servlet, etc..)
    // You might also want to allow some CSS files, etc..
    private boolean isUrlAllowedWithoutLogin(String requestURI) {
        requestURI = requestURI.toLowerCase();

        return (requestURI.endsWith("login.html") || requestURI.endsWith("login.js")
                || requestURI.endsWith("api/login") || requestURI.endsWith("employee_login.html") || requestURI.endsWith("employee_login.js") ||
                requestURI.endsWith("employeeloginservlet") || requestURI.endsWith("stylesheet.css") || requestURI.endsWith("/api/android-login")
                || requestURI.endsWith("search")
                || requestURI.endsWith("abstract_background.jpg"));
    }


    public void init(FilterConfig fConfig) {
    }

    public void destroy() {
    }


}
*/