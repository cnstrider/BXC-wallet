package com.landis.eoswallet.net.model;

import java.util.List;

public class UserAvailableList {
    public List<UserAvailable> rows;
    public boolean more;

    public class UserAvailable {
        public String name;
        public String available;
    }
}
