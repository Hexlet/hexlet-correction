package io.hexlet.typoreporter.web;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Templates {

    static final String CREATE_WKS = "create-workspace";

    static final String INDEX = "index";

    static final String WKS_TEMPLATE_DIR = "workspace";

    static final String WKS_INFO_TEMPLATE = WKS_TEMPLATE_DIR + "/wks-info";

    static final String WKS_SETTINGS_TEMPLATE = WKS_TEMPLATE_DIR + "/wks-settings";

    static final String WKS_TYPOS_TEMPLATE = WKS_TEMPLATE_DIR + "/wks-typos";

    static final String WKS_USERS_TEMPLATE = WKS_TEMPLATE_DIR + "/wks-users";

    static final String WKS_UPDATE_TEMPLATE = WKS_TEMPLATE_DIR + "/wks-update";

}
