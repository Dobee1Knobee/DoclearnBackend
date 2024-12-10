//package com.doclearn.doclearn;
//
//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.doclearn.model.Author;
//import com.doclearn.repository.AuthorRepository;
//import com.doclearn.service.AuthorService;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//@SpringBootTest
//class AuthorServiceTest {
//
//    @Autowired
//    private AuthorService authorService;
//
//    @Autowired
//    private AuthorRepository authorRepository;
//
//    @BeforeEach
//    public void cleanUp() {
//        authorRepository.deleteAll();
//    }
//
//    @Test
//    public void testSaveAuthor() {
//        Author author = new Author("Egor","Biriukov","pass12","egorbiriukov333@gmail.com",LocalDate.now(),"surgeon");
//        assertEquals("Egor", author.getFirstName());
//        assertEquals("Biriukov", author.getLastName());
//        Author savedAuthor = authorService.saveAuthor(author);
//
//        assertNotNull(savedAuthor, "Сохранённый автор не должен быть null");
//        assertNotNull(savedAuthor.getId(), "ID сохранённого автора не должен быть null");
//        assertEquals("Egor", savedAuthor.getFirstName(), "Имя автора должно совпадать");
//    }
//}
