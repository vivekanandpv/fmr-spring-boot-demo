package in.athenaeum.fmrspringbootdemo.controller;

import in.athenaeum.fmrspringbootdemo.exception.DomainValidationException;
import in.athenaeum.fmrspringbootdemo.exception.RecordNotFoundException;
import in.athenaeum.fmrspringbootdemo.service.IBookService;
import in.athenaeum.fmrspringbootdemo.viewmodel.BookCreateViewModel;
import in.athenaeum.fmrspringbootdemo.viewmodel.BookUpdateViewModel;
import in.athenaeum.fmrspringbootdemo.viewmodel.BookViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/books")
public class BookController {
    private final IBookService bookService;

    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookViewModel>> get() {
        return ResponseEntity.ok(bookService.get());
    }

    @GetMapping("{id}")
    public ResponseEntity<BookViewModel> get(@PathVariable int id) {
        return ResponseEntity.ok(bookService.get(id));
    }

    @GetMapping("mt")
    public ResponseEntity<BookViewModel> getMultiThreading() {
        return ResponseEntity.ok(bookService.getFromMultithreading());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BookCreateViewModel viewModel) {
        bookService.create(viewModel);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody BookUpdateViewModel viewModel) {
        bookService.update(viewModel);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        bookService.delete(id);
        return ResponseEntity.ok().build();
    }
}
