package top.backrunner.installstat.core.entity;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
    // 当前页数
    private int currentPage;
    // 每页显示多少个实体
    private int pageSize;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage < 0){
            currentPage = 0;
        }
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        // pageSize最小为20
        if (pageSize < 20){
            pageSize = 20;
        }
        this.pageSize = pageSize;
    }
}
