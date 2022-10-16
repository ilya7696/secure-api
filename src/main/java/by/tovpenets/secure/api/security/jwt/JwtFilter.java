package by.tovpenets.secure.api.config.jwt;

import by.tovpenets.secure.api.config.CustomUserDetails;
import by.tovpenets.secure.api.service.CustomDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import static org.apache.logging.log4j.util.Strings.isNotBlank;
import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtFilter extends GenericFilterBean {

    JwtProvider provider;
    CustomDetailService service;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String bearer = ((HttpServletRequest) servletRequest).getHeader(AUTHORIZATION_HEADER);

        String token = "";
        if (hasText(bearer) && bearer.startsWith(BEARER_PREFIX)) {
            token = bearer.substring(BEARER_PREFIX.length());
        }
        if (isNotBlank(token) && provider.validate(token)) {
            String login = provider.getLoginFromToken(token);
            CustomUserDetails userDetails = service.loadUserByUsername(login);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
            );
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
