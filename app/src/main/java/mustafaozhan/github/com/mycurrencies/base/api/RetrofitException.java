package mustafaozhan.github.com.mycurrencies.base.api;

import android.support.annotation.StringRes;

import java.io.IOException;
import java.lang.annotation.Annotation;

import mustafaozhan.github.com.mycurrencies.R;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:45 PM on Arch Linux wit Love <3.
 */
public final class RetrofitException extends RuntimeException {

    private final String mUrl;
    private final Response mResponse;
    private final Kind mKind;
    private final Retrofit mRetrofit;

    protected RetrofitException(String message, String url, Response response, Kind kind, Throwable exception, Retrofit retrofit) {
        super(message, exception);
        this.mUrl = url;
        this.mResponse = response;
        this.mKind = kind;
        this.mRetrofit = retrofit;
    }

    public static RetrofitException httpError(String url, Response response, Retrofit retrofit) {
        String message = response.code() + " " + response.message();
        return new RetrofitException(message, url, response, Kind.HTTP, null, retrofit);
    }

    public static RetrofitException networkError(IOException exception) {
        return new RetrofitException(exception.getMessage(), null, null, Kind.NETWORK, exception, null);
    }

    public static RetrofitException unexpectedError(Throwable exception) {
        return new RetrofitException(exception.getMessage(), null, null, Kind.UNEXPECTED, exception, null);
    }

    /**
     * The request URL which produced the error.
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Response object containing status code, headers, body, etc.
     */
    public Response getResponse() {
        return mResponse;
    }

    /**
     * The event mKind which triggered this error.
     */
    public Kind getKind() {
        return mKind;
    }

    /**
     * The Retrofit this request was executed on
     */
    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    /**
     * HTTP mResponse body converted to specified {@code type}. {@code null} if there is no
     * mResponse.
     *
     * @throws IOException if unable to convert the body to the specified {@code type}.
     */
    public <T> T getErrorBodyAs(Class<T> type) throws IOException {
        if (mResponse == null || mResponse.errorBody() == null) {
            return null;
        }
        Converter<ResponseBody, T> converter = mRetrofit.responseBodyConverter(type, new Annotation[0]);
        return converter.convert(mResponse.errorBody());
    }

    /**
     * Identifies the event mKind which triggered a {@link RetrofitException}.
     */
    public enum Kind {
        /**
         * An {@link IOException} occurred while communicating to the server.
         */
        NETWORK(R.string.network_error_connection),
        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP(R.string.network_error_server),
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED(R.string.network_error_unexpected);

        private @StringRes
        Integer errorMessage;

        Kind(final Integer errorMessage) {
            this.errorMessage = errorMessage;
        }

        public Integer getErrorMessage() {
            return errorMessage;
        }
    }
}