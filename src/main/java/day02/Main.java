package day02;

import  org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/bookstore?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("klaradb");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not reach data source", sqle);
        }

        Flyway flyway = Flyway.configure().locations("db/migration/bookstore").dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        BookRepository bookRepository = new BookRepository(dataSource);
        bookRepository.insertBook("Fekete István","Vuk",2300,95);
        bookRepository.insertBook("Fekete István","Kele",4300,2);
        bookRepository.insertBook("Fekete István","21 nap",3100,8);

        bookRepository.addBooks(2,5);

        System.out.println(bookRepository.findBooksByPrefix("Feke"));
        System.out.println(bookRepository.findBookById(1));

        System.out.println(bookRepository.insertBookAndGetId("Molnár Ferenc","Pál utcai fiúk",4670,22));

        System.out.println(bookRepository.findBookTitleById(2));
    }
}
