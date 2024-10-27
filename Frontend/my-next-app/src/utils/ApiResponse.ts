import {NextResponse} from "next/server";

export function ApiResponse( isSuccess: boolean, value?: any, errorMessage?: string) {
    return NextResponse.json({
        isSuccess,
        value,
        errorMessage,
    })

}