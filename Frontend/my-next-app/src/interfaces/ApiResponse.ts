// export interface ApiResponse<T> {
//     isSuccess: boolean,
//     object?: T,
//     errorMessage?: string
// }

interface Success<T> {
    value: T;
}

interface Failure<T> {
    value?: T;
    errorMessage: string;
}

type ApiResponse<T> = Success<T> | Failure<T>;

// Something called type guards to implement type checking

function isSuccess<T>(response: ApiResponse<T>): response is Success<T> {
    return 'value' in response && !('httpStatus' in response)
}

function isFailure<T>(response: ApiResponse<T>): response is Failure<T> {
    return 'httpStatus' in response && 'errorMessage' in response;
}