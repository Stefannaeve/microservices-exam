'use client'

import React, {useEffect, useState} from "react";
import {getBooks} from "@/lib/talkToApi"
import {ApiResponse} from "@/interfaces/ApiResponse";
import clsx from 'clsx'


export default function Home() {
    const [something, setSomething] = useState()

    const findBooks = async () => {
        try {
            const response: ApiResponse = await getBooks()
            console.log("Response", response)
            setSomething(response.object)
            console.log(something)
        } catch (error) {
            console.error("Error")
        }
    }

    useEffect(() => {
    }, [something])

    const printBooks = () => {
        return <div>{something}</div>
    }

  return (
    <div className="">
      <button className={"w-fit"} onClick={findBooks}>Find books</button>
        <div className={clsx(something ? "": "hidden")}>Hallo</div>
        {printBooks()}
    </div>
  );
}
