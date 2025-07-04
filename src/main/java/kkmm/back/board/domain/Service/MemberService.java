package kkmm.back.board.domain.Service;

import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Transactional
    public Long join(String name, String email, String rawPassword) {

        String encodedPassword = passwordEncoder.encode(rawPassword);
        Member member = new Member(name, email, encodedPassword);

        validateDuplicateMember(member);
        memberRepository.save(member);

        return member.getId();
    }

    public Member findById(Long id) {
        Locale locale = LocaleContextHolder.getLocale();

        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException (
                        messageSource.getMessage("login.error.badCredentials", null, locale)));
    }

    public Member findByEmail(String email) {
        Locale locale = LocaleContextHolder.getLocale();

        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(
                        messageSource.getMessage("login.error.badCredentials", null, locale)));
    }
    
    
    public Member login(String email, String rawPassword) {
        Locale locale = LocaleContextHolder.getLocale();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageSource.getMessage("login.error.badCredentials", null, locale)));

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new BadCredentialsException(
                    messageSource.getMessage("login.error.badCredentials", null, locale));
        }

        return member;
    }


    private void validateDuplicateMember(Member member) {
        Locale locale = LocaleContextHolder.getLocale();

        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalStateException(messageSource.getMessage("login.error.duplicateEmail", null, locale));
        }
    }
}
