package org.erensekkeli.chatbotservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.erensekkeli.chatbotservice.dto.AuthResponse;
import org.erensekkeli.chatbotservice.dto.CustomerDTO;
import org.erensekkeli.chatbotservice.entity.Customer;
import org.erensekkeli.chatbotservice.entity.Token;
import org.erensekkeli.chatbotservice.exceptions.AuthorizationException;
import org.erensekkeli.chatbotservice.exceptions.ItemNotFoundException;
import org.erensekkeli.chatbotservice.mapper.CustomerMapper;
import org.erensekkeli.chatbotservice.repository.TokenRepository;
import org.erensekkeli.chatbotservice.request.LoginRequest;
import org.erensekkeli.chatbotservice.util.RandomKeyGenerator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    private final long THREE_MONTH = (long) 90 * 24 * 60 * 60 * 1000;

    public AuthResponse authenticate(LoginRequest request) {
        Optional<Customer> customer = customerService.findByUsername(request.getUsername());
        if (customer.isEmpty()) {
            throw new ItemNotFoundException("Customer not found with username: " + request.getUsername());
        }

        boolean isMatched = passwordEncoder.matches(request.getPassword(), customer.get().getPassword());
        if (!isMatched) {
            throw new AuthorizationException("Password is incorrect");
        }

        String token = RandomKeyGenerator.generateRandomToken();
        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setCustomer(customer.get());
        tokenRepository.save(tokenEntity);

        AuthResponse authResponse = new AuthResponse();
        CustomerDTO customerDTO = CustomerMapper.INSTANCE.convertToCustomerDTO(customer.get());
        authResponse.setToken(token);
        authResponse.setCustomer(customerDTO);

        return authResponse;
    }

    @Transactional
    public UserDetails getUserDetails(String token) {
        Optional<Token> inDbToken = tokenRepository.findById(token);
        return inDbToken.<UserDetails>map(Token::getCustomer).orElse(null);
    }

    @Scheduled(fixedRate = THREE_MONTH)
    public void cleanExpiredTokens() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Token> expiredTokens = tokenRepository.findByDateBefore(oneMonthAgo);
        tokenRepository.deleteAll(expiredTokens);
    }

    public void clearToken(String token) {
        Optional<Token> inDbToken = tokenRepository.findById(token);
        inDbToken.ifPresent(tokenRepository::delete);
    }
}
