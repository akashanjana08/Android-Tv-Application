package shop.tv.rsys.com.tvapplication.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by akash.sharma on 12/14/2017.
 */
@Root(name="BTAResponse" ,strict=false)
public class BtaResponseModel {

    @ElementList(name="Movie", inline=true)
    @Path("Movies")
    private List<Movie> Movie;




    public List<BtaResponseModel.Movie> getMovie() {
        return Movie;
    }



    public void setMovie(List<BtaResponseModel.Movie> movie) {
        Movie = movie;
    }

    @Root(name="Movie" ,strict=false)
    public static class Movie
    {
        @Element(name = "Subtype")
        private String SubType;
        @Element(name = "ID")
        private String movieId;
        @Element(name = "Title")
        private String title;
        @Element(name = "longDescription")
        private String longDescription;
        @Element(name = "JPEG")
        private String posterImage;

        public String getSubType() {
            return SubType;
        }

        public void setSubType(String subType) {
            SubType = subType;
        }

        public String getMovieId() {
            return movieId;
        }

        public void setMovieId(String movieId) {
            this.movieId = movieId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLongDescription() {
            return longDescription;
        }

        public void setLongDescription(String longDescription) {
            this.longDescription = longDescription;
        }

        public String getPosterImage() {
            return "http://10.67.191.50:8085/posterserver/poster/vod/w-274_h-364/"+posterImage;
        }

        public void setPosterImage(String posterImage) {
            this.posterImage = posterImage;
        }
    }
}


