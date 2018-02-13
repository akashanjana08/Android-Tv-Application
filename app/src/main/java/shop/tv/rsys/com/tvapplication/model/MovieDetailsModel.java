package shop.tv.rsys.com.tvapplication.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by akash.sharma on 2/12/2018.
 */
@Root(name="MOVIELIST" ,strict=false)
public class MovieDetailsModel {

    @ElementList(name="Movie", inline=true , required = false)
    private List<Movie> Movie;


    public List<MovieDetailsModel.Movie> getMovie() {
        return Movie;
    }

    public void setMovie(List<MovieDetailsModel.Movie> movie) {
        Movie = movie;
    }


    @Root(name="Movie" ,strict=false)
    public static class Movie
    {
        @Element(name = "ID")
        private String movieId;

        @Element(name = "Title")
        private String title;

        @Element(name = "SortName")
        private String sortName;

        @Element(name="longDescription")
        private String longDescription;

        @Element(name="JPEG")
        private String posterImage;

        public String getPosterImage() {

           // return posterImage;
            return "http://10.67.191.50:8085/posterserver/poster/vod/w-274_h-364/sky_90103936602002frm03.jpg";
        }

        public void setPosterImage(String posterImage) {
            this.posterImage = posterImage;
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

        public String getSortName() {
            return sortName;
        }

        public void setSortName(String sortName) {
            this.sortName = sortName;
        }

        public String getLongDescription() {
            return longDescription;
        }

        public void setLongDescription(String longDescription) {
            this.longDescription = longDescription;
        }
    }

}
