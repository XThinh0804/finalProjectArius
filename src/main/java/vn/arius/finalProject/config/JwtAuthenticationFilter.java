package vn.arius.finalProject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.arius.finalProject.dto.RestResponse;
import vn.arius.finalProject.service.TokenRevokedService;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenRevokedService tokenRevokedService;
    private final ObjectMapper mapper;

    public JwtAuthenticationFilter(TokenRevokedService tokenRevokedService, ObjectMapper mapper) {
        this.tokenRevokedService = tokenRevokedService;
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);
        if (this.tokenRevokedService.existsToken(token)) {
            response.setContentType("application/json;charset=UTF-8");
            RestResponse<Object> errorResponse = new RestResponse<>();
            errorResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
            errorResponse.setError("An error occurred while attempting to decode Jwt: The code has been revoked");
            errorResponse.setMessage("Token này đã được thu hồi!");
            errorResponse.setData(null);
            mapper.writeValue(response.getWriter(), errorResponse);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
