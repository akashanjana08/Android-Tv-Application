package shop.tv.rsys.com.tvapplication.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import shop.tv.rsys.com.tvapplication.Movie;
import shop.tv.rsys.com.tvapplication.model.BtaResponseModel;

/**
 * Created by akash.sharma on 12/15/2017.
 */

public class MovieListParsing {

    public static List<Movie> getMovieList(List<BtaResponseModel.Movie> btaMovieList)
    {
              List<Movie> movieList = new ArrayList<>();
              Iterator<BtaResponseModel.Movie>  iterator =  btaMovieList.iterator();
              while (iterator.hasNext())
              {
                  Movie movie = new Movie();
                  BtaResponseModel.Movie  btaMovie = iterator.next();
                  movie.setStudio(btaMovie.getTitle());
                  movie.setTitle(btaMovie.getLongDescription());
                  movie.setCardImageUrl(btaMovie.getPosterImage());
                  movieList.add(movie);
              }
        return  movieList;
    }
}
