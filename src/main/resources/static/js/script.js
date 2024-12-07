document.getElementById("upBookForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const form = e.target;
    const token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJidWlodXVkdXkuY29tIiwic3ViIjoiaHV1ZHV5IiwiZXhwIjoxNzMzNjI1MjUxLCJjdXN0b21DbGFpbSI6IkN1c3RvbSBDbGFpbSIsImlhdCI6MTczMzUzODg1MX0.F_uefmXJ7Fph4a1ZlVNz-Tr7rcRoKvq2Hk0ehrzTHkVXTXLOdqr69Qudg6mmuFLQU8zVmU5T8olERQoFZU4tMw"; // Replace with the actual JWT token

    // Collect form data
    const bookData = {
        bookName: form.bookName.value,
        bookDescription: form.bookDescription.value,
        bookImage: form.bookImage.value,
        publishedDate: form.publishedDate.value,
        bookFormat: form.bookFormat.value,
        bookSaleLink: form.bookSaleLink.value,
        languageId: parseInt(form.languageId.value),
        bookAuthor: form.bookAuthor.value,
        categoryId: Array.from(form.categoryId.selectedOptions).map(option => parseInt(option.value)),
    };

    // Send data to the API
    fetch("/up-book", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(bookData),
    })
        .then(response => response.json())
        .then(data => {
            const message = data.message || "Book uploaded successfully!";
            console.log(message);
            alert(message);
        })
        .catch(error => {
            console.error("Error:", error);
            alert("An error occurred while uploading the book.");
        });
});
