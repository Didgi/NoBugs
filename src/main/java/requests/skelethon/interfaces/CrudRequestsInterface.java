package requests.skelethon.interfaces;

import models.BaseModel;

public interface CrudRequestsInterface {
    Object POST(BaseModel baseModel);

    Object GET();

    Object PUT(BaseModel baseModel);

    Object DELETE(int id);
}
