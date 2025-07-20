package kkmm.back.board.domain.Service;

import kkmm.back.board.domain.dto.NoteDto;
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
    public Long save(NoteDto noteDto, Member member, Category category) {

        List<UploadFile> uploadFiles = fileManager.storeFiles(noteDto.getFiles());
        List<UploadFile> uploadImageFiles = fileManager.storeFiles(noteDto.getImages());

        Note note = new Note(noteDto, member, category, uploadFiles, uploadImageFiles);

        category.increaseCount(); // 카테고리 사용 수 증가
        noteRepository.save(note); // 노트 저장

        return note.getId();
    }

    public Note findById(Long id) {
        return noteRepository.findById(id).orElseThrow();
    }

    public List<Note> findNotes(int page) {
        return noteQueryRepository.findNoteRange((page - 1) * 10, 10);
    }

    public List<Note> searchNotes(int page, String keyword, String searchType) {
        return noteQueryRepository.searchNotesRange(searchType, keyword, (page - 1) * 10, 10);
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
    public void updateNote(Long id, NoteDto noteDto) {
        Note note = noteRepository.findById(id).orElseThrow();

        List<UploadFile> uploadFiles = fileManager.storeFiles(noteDto.getFiles());
        List<UploadFile> uploadImageFiles = fileManager.storeFiles(noteDto.getImages());

        fileManager.deleteFiles(noteDto.getDeleteFiles());
        fileManager.deleteFiles(noteDto.getDeleteImages());

        note.updateContent(noteDto, uploadFiles, uploadImageFiles);
    }

    public Long countTotalPages() {
        return (noteRepository.count() / 10) + 1;
    }

    public Long countSearchedPages(String keyword, String searchType) {
        return (noteQueryRepository.countSearchedNotes(searchType, keyword) / 10) + 1;
    }
}
