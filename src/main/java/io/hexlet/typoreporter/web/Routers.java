package io.hexlet.typoreporter.web;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Routers {

    public static final String ID = "/{id}";

    public static final String API = "/api";

    public static final String TYPOS = "/typos";

    public static final String API_TYPOS = API + TYPOS;

    public static final String TYPO_SORT = "createdDate";
}
