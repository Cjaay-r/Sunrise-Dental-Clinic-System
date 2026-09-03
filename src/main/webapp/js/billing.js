const printReceiptButton =
    document.getElementById("printReceiptButton");

if (printReceiptButton) {
    printReceiptButton.addEventListener(
        "click",
        function () {
            window.print();
        }
    );
}