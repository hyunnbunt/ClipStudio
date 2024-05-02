//package projects.seller.ClipStudio.batch;
//
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.NonTransientResourceException;
//import org.springframework.batch.item.ParseException;
//import org.springframework.batch.item.UnexpectedInputException;
//import org.springframework.batch.item.database.JdbcCursorItemReader;
//import projects.seller.ClipStudio.Entity.Video;
//import projects.seller.ClipStudio.repository.VideoRepository;
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
