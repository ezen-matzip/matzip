// 메뉴 추가버튼
const $menu = document.getElementById("menu");
let num = $menu.querySelectorAll("tr").length;

function addMenu() {
    let newMenu = $menu.insertRow();
    let input1 = newMenu.insertCell(0);
    let input2 = newMenu.insertCell(1);
    let input3 = newMenu.insertCell(2);
    let input4 = newMenu.insertCell(3);

    num++;

    // 셀 내부에 직접 <input> 태그 추가
    input1.innerHTML = `<input type="radio" id="대표메뉴" name="대표메뉴" value="메뉴${num}">`
    input2.innerHTML = `<input class="store-input-small" type="text" id="메뉴${num}" name="메뉴${num}" placeholder="메뉴명">`;
    input3.innerHTML = '---------- '
    input4.innerHTML = `<input class="store-input-small" type="text" id="메뉴${num}가격" name="메뉴${num}가격" placeholder="가격">원`;
}

// 메뉴 삭제버튼

function deleteMenu() {
    if (num > 2) {
        $menu.deleteRow($menu.rows.length - 1);
        num--;
    } else
        alert("더 이상 삭제할 수 없습니다!");
}
