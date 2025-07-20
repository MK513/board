package kkmm.back.board.domain.Service;

import kkmm.back.board.domain.dto.SignupDto;
import kkmm.back.board.domain.model.Comment;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.domain.repository.CommentRepository;
import kkmm.back.board.domain.repository.MemberRepository;
import kkmm.back.board.domain.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final NoteRepository noteRepository;
    private final CommentRepository commentRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Transactional
    public Long join(SignupDto signupDto) {

        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());
        Member member = new Member(signupDto.getName(), signupDto.getEmail(), encodedPassword);

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
                        messageSource.getMessage("login.error.notExist", null, locale)));

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new BadCredentialsException(
                    messageSource.getMessage("login.error.badCredentials", null, locale));
        }

        return member;
    }

    public Page<Note> findNotes(Long memberId, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return noteRepository.findByMemberId(memberId, pageable);
    }

    public Page<Comment> findComments(Long memberId, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentRepository.findByMemberId(memberId, pageable);
    }

    public int countNotes(Long memberId) {
        return noteRepository.countByMemberId(memberId);
    }

    public int countComments(Long memberId) {
        return commentRepository.countByMemberId(memberId);
    }

    private void validateDuplicateMember(Member member) {
        Locale locale = LocaleContextHolder.getLocale();

        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalStateException(messageSource.getMessage("login.error.duplicateEmail", null, locale));
        }
    }
}
