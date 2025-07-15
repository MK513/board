package kkmm.back.board;

import kkmm.back.board.domain.Service.CategoryService;
import kkmm.back.board.domain.Service.MemberService;
import kkmm.back.board.domain.Service.NoteService;
import kkmm.back.board.domain.repository.MemberRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

	//@Bean
	@Profile("local")
	public TestDataInit testDataInit(MemberService memberService,
									 NoteService noteService,
									 CategoryService categoryService) {
		return new TestDataInit(memberService, noteService, categoryService);
	}
}
