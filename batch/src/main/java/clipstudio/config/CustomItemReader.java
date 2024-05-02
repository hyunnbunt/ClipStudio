//package clipstudio.batch;
//
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.NonTransientResourceException;
//import org.springframework.batch.item.ParseException;
//import org.springframework.batch.item.UnexpectedInputException;
//import org.springframework.batch.item.database.JdbcCursorItemReader;
//import clipstudio.Entity.Video;
//import clipstudio.repository.VideoRepository;
//
//public class CustomItemReader<Video> extends JdbcCursorItemReader<Video> {
//
//    @Override
//    public Video read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
//
//        videoRepository.findByNumber()
//        return null;
//    }
//}
