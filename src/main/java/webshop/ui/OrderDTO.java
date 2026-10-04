package webshop.ui;

import java.util.List;

/**
 * En order som den visas för användaren.
 */
public class OrderDTO {

    private final int id;
    private final String created;
    private final int total;
    private final String status;
    private final List<OrderLineDTO> lines;

    public OrderDTO(int id, String created, int total, String status, List<OrderLineDTO> lines) {
        this.id = id;
        this.created = created;
        this.total = total;
        this.status = status;
        this.lines = lines;
    }

    public int getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public int getTotal() {
        return total;
    }

    /** "Packas" eller "Skickad". */
    public String getStatus() {
        return status;
    }

    public List<OrderLineDTO> getLines() {
        return lines;
    }
}
