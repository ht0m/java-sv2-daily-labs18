package day02;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class BookRepository {

    private JdbcTemplate jdbcTemplate;

    public BookRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insertBook(String writer, String title, int price, int pieces) {
        jdbcTemplate.update("insert into books(writer,title,price,pieces) values (?,?,?,?)", writer, title, price, pieces);
    }

    public void addBooks(long id, int pieces) {
        jdbcTemplate.update("update books set pieces = pieces+? where id =?", pieces, id);
    }

    public long insertBookAndGetId(String writer, String title, int price, int pieces) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("insert into books(writer,title,price,pieces) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, writer);
            ps.setString(2, title);
            ps.setInt(3, price);
            ps.setInt(4, pieces);
            return ps;
        }, holder);
        return holder.getKey().longValue();
    }

    public List<Book> findBooksByPrefix(String prefix) {
        return jdbcTemplate.query("select * from books where writer like ?",
                (rs, rowNumber) -> new Book(rs.getLong("id"), rs.getString("writer"), rs.getString("title"), rs.getInt("price"), rs.getInt("pieces")),
                prefix + "%");
    }

    public Book findBookById(long id) {
        return jdbcTemplate.queryForObject("select * from books where id=?",
                (rs, rowNumber) -> new Book(rs.getLong("id"), rs.getString("writer"), rs.getString("title"), rs.getInt("price"), rs.getInt("pieces")),
                id);
    }

    public String findBookTitleById(long id) {
        return jdbcTemplate.queryForObject("select title from books where id=?", String.class, id);

    }
}
