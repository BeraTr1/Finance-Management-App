package user.saulo.data;

import java.sql.SQLException;

public interface Data {
    public void loadAll() throws Exception;

    public void saveAll() throws Exception;
}
