export interface ApiResponse<T> {
    isSuccess: boolean,
    object?: T,
    errorMessage?: string
}