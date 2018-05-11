package com.gm.shared.jpa.eclipselink.rest.spring.filter;

import com.gm.shared.jpa.eclipselink.rest.persistence.JpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.gm.shared.jpa.eclipselink.rest.constant.JpaEclipseLinkConstant.CURRENT_HOST_URI;
import static com.gm.shared.jpa.eclipselink.rest.constant.JpaEclipseLinkConstant.CURRENT_HTTP_METHOD;

@ConditionalOnProperty(name = "gm.shared.jpa.spring.rest.enabled", havingValue = "true")
@Component
public class JpaFilter extends OncePerRequestFilter {

    @Autowired
    private JpaService jpa;

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        jpa.getEmf().setProperty(CURRENT_HOST_URI, request.getRequestURL().toString());
        jpa.getEmf().setProperty(CURRENT_HTTP_METHOD, request.getMethod());

        filterChain.doFilter(request, response);
    }

}
