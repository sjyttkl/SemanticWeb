package FriendTracker.Upcoming;

public class UpcomingException extends RuntimeException
{
   public UpcomingException(Throwable t)
   {
      super(t);
   }
   
   public UpcomingException(String message, Throwable t)
   {
      super(message, t);
   }
   
   public UpcomingException(String message)
   {
      super(message);
   }
}
