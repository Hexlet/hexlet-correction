package io.hexlet.typoreporter.web;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Routers {

    public static final String API = "/api";

    public static final String ID_PATH = "/{id}";

    public static final String UPDATE = "/update";

    public static final String CREATE = "/create";

    public static final String SETTINGS = "/settings";

    public static final String REDIRECT = "redirect:";

    public static final String REDIRECT_ROOT = REDIRECT + "/";

    public static final String DEFAULT_SORT_FIELD = "createdDate";

    public static final String USERS = "/users";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Workspace {

        public static final String API_WORKSPACES = API + "/workspaces";

        public static final String WORKSPACE = "/workspace";

        public static final String WKS_NAME_PATH = "/{wksName}";

        public static final String REDIRECT_WKS_ROOT = REDIRECT + WORKSPACE + "/";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Typo {

        public static final String TYPOS = "/typos";

        public static final String TYPO_STATUS = "/status";
    }
}
