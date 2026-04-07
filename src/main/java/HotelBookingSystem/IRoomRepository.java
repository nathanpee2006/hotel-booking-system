package HotelBookingSystem;

import java.util.List;

public interface IRoomRepository {

    public Room getRoomById(int id);

    public void updateRoom(Room room);

    public List<Room> getAllRooms();

}