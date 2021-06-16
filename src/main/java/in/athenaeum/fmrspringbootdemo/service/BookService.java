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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

    @Override
    public BookViewModel getFromMultithreading() {
        ExecutorService service = Executors.newFixedThreadPool(3);

        Future<String> titleFuture = service.submit(this::getTitle);
        Future<Integer> nPagesFuture = service.submit(this::getNPages);
        Future<Double> priceFuture = service.submit(this::getPrice);

        BookViewModel viewModel = new BookViewModel();
        viewModel.setAuthor("Default Author");
        viewModel.setEdition(2);
        viewModel.setId(1001);

        try {
            viewModel.setnPages(nPagesFuture.get());
            viewModel.setPrice(priceFuture.get());
            viewModel.setTitle(titleFuture.get());

            return viewModel;
        } catch (InterruptedException|ExecutionException exception) {
            exception.printStackTrace();
            throw new RuntimeException("Execution exception");
        } finally {
            service.shutdown();
        }
    }

    private String getTitle() throws InterruptedException {
        Thread.sleep(500);
        return "Book title from multithreading";
    }

    private int getNPages() throws InterruptedException {
        Thread.sleep(500);
        return 456;
    }

    private double getPrice() throws InterruptedException {
        Thread.sleep(500);
        return 789.56;
    }
}
