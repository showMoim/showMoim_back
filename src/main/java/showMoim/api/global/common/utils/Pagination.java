package showMoim.api.global.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Pagination {
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 100;

    public static Pageable of(int page) {
        return PageRequest.of(getPage(page) - 1, DEFAULT_SIZE);
    }

    public static Pageable of(int page, int size) {
        return PageRequest.of(getPage(page) - 1, getSize(size));
    }

    private static int getPage(int page) {
        return page <= 0 ? 1 : page;
    }

    private static int getSize(int size) {
        return size > MAX_SIZE ? DEFAULT_SIZE : size;
    }
}
