package za.co.invoke.solutions.naturalgasprices.helper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class ListToPageConverter {

    /**
     * Private constructor to hide implicit public constructor
     */
    private ListToPageConverter() {
    }

    public static <T> Page<T> convert(final List<T> list, final int pageNo, final int pageSize) {
        final int end = Math.min(pageNo + pageSize, list.size());
        final List<T> sublist = list.subList(pageNo, end);
        return new PageImpl<>(sublist, PageRequest.of(pageNo, pageSize), list.size());
    }
}