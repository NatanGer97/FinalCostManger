package costmanager;

public class User
{
    private String userName;
    private  String userPassword;

    public User(String userName, String userPassword)
    {
       setUserName(userName);
       setUserPassword(userPassword);
    }

    public String getUserName()
    {
        return userName;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "userName='" + getUserName() + '\'' +
                ", userPassword='" + getUserPassword() + '\'' +
                '}';
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserPassword()
    {
        return userPassword;
    }

    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }
}
