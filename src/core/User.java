package core;

import models.Ticket;

public interface User {
    //توی اینترفیس فقط متودا رو مینویسیم یا اتریبیوت ها رو هم مینویسیم؟

    void register();

    int creatTicket();

    Ticket[] Mytickets();

    Ticket searchTicket();

}
