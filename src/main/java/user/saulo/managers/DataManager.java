package user.saulo.managers;

import user.saulo.data.Data;

public class DataManager {
    private Data data;

    public DataManager(Data data) {
        this.data = data;
    }

    public void saveData() throws Exception {
        data.saveAll();
    }

    public void loadData() throws Exception {
        data.loadAll();
    }
}
