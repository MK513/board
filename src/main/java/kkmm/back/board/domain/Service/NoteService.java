package kkmm.back.board.domain.Service;

import jakarta.validation.constraints.NotEmpty;
import kkmm.back.board.domain.model.Category;
import kkmm.back.board.domain.model.Member;
import kkmm.back.board.domain.model.Note;
import kkmm.back.board.domain.model.UploadFile;
import kkmm.back.board.domain.repository.CategoryRepository;
import kkmm.back.board.domain.repository.NoteQueryRepository;
import kkmm.back.board.domain.repository.NoteRepository;
import kkmm.back.board.web.dto.NoteForm;
import kkmm.back.board.web.file.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteQueryRepository noteQueryRepository;
    private final CategoryRepository categoryRepository;
    private final FileManager fileManager;

    @Transactional
    public Long saveNote(NoteForm noteForm, Member member, Category category) {

        List<UploadFile> uploadFiles = fileManager.storeFiles(noteForm.getAttachFile());
        List<UploadFile> uploadImageFiles = fileManager.storeFiles(noteForm.getAttachImageFile());

        Note note = new Note(noteForm, member, category, uploadFiles, uploadImageFiles);

        noteRepository.save(note); // 노트 저장
        categoryRepository.increaseCount(note.getCategory().getId()); // 카테고리 사용 수 증가

        return note.getId();
    }

    public Note findOne(Long id) {
        return noteRepository.findById(id).orElseThrow();
    }

    public List<Note> findPage(int page) {
        return noteQueryRepository.findNoteRange((page - 1) * 10, 10);
    }

    public List<Note> searchPage(int page, String keyword, String searchType) {
        return noteQueryRepository.searchNotesRange(searchType, keyword, (page - 1) * 10, 10);
    }

    public Long countNote() {
        return noteRepository.count();
    }

    public Long searchNoteCount(String keyword, String searchType) {
        return noteQueryRepository.countSearchedNotes(keyword, searchType);
    }

    @Transactional
    public void increaseViewCount(Long id) {
        noteRepository.increaseViewCount(id);
    }

    @Transactional
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    @Transactional
    public void updateNote(Long id, @NotEmpty String title, @NotEmpty String content) {
        Note note = noteRepository.findById(id).orElseThrow();
        note.updateContent(title, content);
        noteRepository.save(note);
    }
}
