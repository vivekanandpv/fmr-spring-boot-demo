package in.athenaeum.fmrspringbootdemo.service;

import in.athenaeum.fmrspringbootdemo.viewmodel.BookCreateViewModel;
import in.athenaeum.fmrspringbootdemo.viewmodel.BookUpdateViewModel;
import in.athenaeum.fmrspringbootdemo.viewmodel.BookViewModel;

import java.util.List;

public interface IBookService {
    List<BookViewModel> get();
    BookViewModel get(int id);
    void create(BookCreateViewModel viewModel);
    void update(BookUpdateViewModel viewModel);
    void delete(int id);
    BookViewModel getFromMultithreading();
}
