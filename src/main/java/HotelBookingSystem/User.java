package HotelBookingSystem;

public abstract class User {
	
    private int userId; // TODO: consider removing

    private String name; 

    private String email; 

    private String password; 
    
    
    public User(int userId, String name, String email) { 

        this.userId = userId; 

        this.name = name; 

        this.email = email; 
    } 
    
    
    public int getUserId() { 

        return userId; 
    } 
    
    
    public String getName() { 

        return name; 
    } 

  

    public String getEmail() { 

        return email; 
    } 

}
