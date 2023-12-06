package client.dto;

import java.io.Serializable;

public class getRdto implements Serializable {
    int idx;

    public getRdto(int idx) {
        this.idx = idx;
    }
}