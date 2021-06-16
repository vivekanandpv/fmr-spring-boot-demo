package in.athenaeum.fmrspringbootdemo.service;

import in.athenaeum.fmrspringbootdemo.exception.DomainValidationException;
import in.athenaeum.fmrspringbootdemo.exception.RecordNotFoundException;
import in.athenaeum.fmrspringbootdemo.model.Book;
import in.athenaeum.fmrspringbootdemo.repository.BookRepository;
import in.athenaeum.fmrspringbootdemo.viewmodel.BookCreateViewModel;
import in.athenaeum.fmrspringbootdemo.viewmodel.BookUpdateViewModel;
import in.athenaeum.fmrspringbootdemo.viewmodel.BookViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService implements IBookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BookViewModel> get() {
        return repository.findAll()
                .stream()
                .map(b -> {
                    BookViewModel viewModel = new BookViewModel();
                    BeanUtils.copyProperties(b, viewModel);
                    return viewModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public BookViewModel get(int id) {
        Book bookDb = repository.findById(id)
                .orElseThrow(RecordNotFoundException::new);

        BookViewModel viewModel = new BookViewModel();
        BeanUtils.copyProperties(bookDb, viewModel);
        return viewModel;
    }

    @Override
    public void create(BookCreateViewModel viewModel) {
        if (viewModel.getTitle().strip().length() < 5) {
            throw new DomainValidationException();
        }

        Book book = new Book();
        BeanUtils.copyProperties(viewModel, book);

        book.setHsnCode("HSN234433");
        book.setSku("SKU223111222");
        book.setSecretKey(UUID.randomUUID().toString());

        repository.saveAndFlush(book);
    }

    @Override
    public void update(BookUpdateViewModel viewModel) {
        //  check whether the id exists
        Book bookDb = repository.findById(viewModel.getId())
                .orElseThrow(RecordNotFoundException::new);


        //  check the validity of the fields
        if (viewModel.getTitle().strip().length() < 5) {
            throw new DomainValidationException();
        }

        //  apply the update
        BeanUtils.copyProperties(viewModel, bookDb);

        //  save and flush
        repository.saveAndFlush(bookDb);
    }

    @Override
    public void delete(int id) {
        //  check whether the id exists
        Book bookDb = repository.findById(id)
                .orElseThrow(RecordNotFoundException::new);
        //  delete
        repository.delete(bookDb);
    }
}
