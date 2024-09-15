import {NextResponse} from "next/server";
import {ApiResponse} from "@/interfaces/ApiResponse";

export function FApiResponse(response: ApiResponse<any>) {
    return NextResponse.json({
        response
    })

}