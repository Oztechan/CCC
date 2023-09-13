//
//  PremiumView.swift
//  CCC
//
//  Created by Mustafa Ozhan on 21.08.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Res
import Provider
import SwiftUI

struct PremiumView: View {
    @Environment(\.colorScheme) private var colorScheme

    var event: PremiumEvent
    var state: PremiumState

    var body: some View {
        ZStack {
            Color(\.background_strong).edgesIgnoringSafeArea(.all)

            VStack {
                Text(String(\.txt_premium))
                    .font(relative: .title2)
                    .padding(4.cp())
                    .padding(.top, 10.cp())

                if state.loading {
                    FormProgressView()
                } else {
                    Form {
                        List(state.premiumTypes, id: \.data) { premiumType in
                            PremiumItemView(item: premiumType)
                                .onTapGesture {
                                    event.onPremiumItemClick(type: premiumType)
                                }
                                .frame(minWidth: 0, maxWidth: .infinity, alignment: .center)
                        }
                        .listRowInsets(.init())
                        .listRowBackground(Color(\.background))

                        PremiumItemView(item: nil)
                            .listRowInsets(.init())
                            .listRowBackground(Color(\.background))
                    }
                    .withClearBackground(color: Color(\.background))
                }

                Spacer()
            }.navigationBarHidden(true)
        }
    }
}
